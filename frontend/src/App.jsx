import { BrowserRouter, Routes, Route } from "react-router-dom";
import { useEffect, useState } from "react";
import { logout, getCurrentUser } from "./api/auth";
import ProductsPage from "./pages/ProductsPage";
import ProductDetailsPage from "./pages/ProductDetailsPage";
import CreateProductPage from "./pages/CreateProductPage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import MyReviewsPage from "./pages/MyReviewsPage";
import ImportPreviewPage from "./pages/ImportPreviewPage";
import Navbar from "./components/Navbar";
import AdminRoute from "./components/AdminRoute";
import ProtectedRoute from "./components/ProtectedRoute";
import LoadingState from "./components/LoadingState";

function App() {
  const [currentUser, setCurrentUser] = useState(null);
  const [authLoading, setAuthLoading] = useState(true);
  const [authError, setAuthError] = useState("");
  const [logoutError, setLogoutError] = useState("");
  async function handleLogout() {
    try {
      await logout();
      // When this prop is changed not all possible pages re-renders
      // It only renders the page for the current URL plus other components under app outside of routes
      setCurrentUser(null);
    } catch (err) {
      setLogoutError(err.message || "Could not log out. Please try again.");
    }
  }
  // The browser keeps the session id cookie even when refreshed
  // and React uses /auth/me after refresh to ask the backend which user that session belongs to.
  useEffect(() => {
    async function checkSession() {
      try {
        setAuthError("");

        const user = await getCurrentUser();
        setCurrentUser(user);
      } catch (err) {
        setCurrentUser(null);
        setAuthError(
          err.message || "Unable to verify your session. Please try again.",
        );
      } finally {
        setAuthLoading(false);
      }
    }

    checkSession();
    // If you put it in App.jsx inside a useEffect with an empty dependency array:
    // then it runs once when App first mounts, not every re-render.
  }, []);

  if (authLoading) {
    <main className="container">
      <LoadingState message="Checking your session..." />
    </main>;
  }

  return (
    // BrowserRouter connects your React app to the Browser's URL bar
    // React router can look at the URL and decide what component to display
    // Need for Link to = and path = to work

    // this is basically the main class of the app
    // Rotutes decides which page to show based on url
    // Navbar is everywhere because it's outside of routes
    <BrowserRouter>
      {/* Passing currentUser and handleLogout function as props to navbar */}
      <Navbar currentUser={currentUser} handleLogout={handleLogout} />
      {authError && (
        <p className="error-message" role="alert">
          {authError}
        </p>
      )}
      {logoutError && <p className="error-message">{logoutError}</p>}
      <Routes>
        {/* These are just paths to where we have these pages not same as APIs */}
        <Route path="/" element={<ProductsPage />}></Route>
        <Route path="/products" element={<ProductsPage />}></Route>
        {/* This needs to go before Product details route so it doesnt think 'new' is a product id */}
        <Route
          path="/products/new"
          element={
            <AdminRoute currentUser={currentUser}>
              <CreateProductPage />
            </AdminRoute>
          }
        ></Route>
        <Route
          path="/products/:id"
          element={<ProductDetailsPage currentUser={currentUser} />}
        ></Route>
        <Route
          path="/login"
          // Need that function to pass to LoginPage not Route
          // That is why we pass it in there instead of as another attribute of Route
          element={<LoginPage setCurrentUser={setCurrentUser} />}
        ></Route>
        <Route
          path="/register"
          element={<RegisterPage setCurrentUser={setCurrentUser} />}
        ></Route>
        <Route
          path="/my-reviews"
          element={
            <ProtectedRoute currentUser={currentUser}>
              <MyReviewsPage currentUser={currentUser} />
            </ProtectedRoute>
          }
        ></Route>
        <Route
          path="/import"
          element={
            <AdminRoute currentUser={currentUser}>
              <ImportPreviewPage />
            </AdminRoute>
          }
        ></Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
