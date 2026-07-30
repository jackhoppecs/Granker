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
      setLogoutError("");
      setAuthError("");

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

  // listen for session expired event
  // Empty dependency array means this runs once when App mounts
  // This is because we only want to register one global session listener
  // Without useEffect registering directly inside the component body could happen on every render and create multiple listeners
  useEffect(() => {
    function handleSessionExpired() {
      setCurrentUser(null);
      setAuthError("Your session has expired. Please log in again.");
    }

    // Listens for the event "session-expired" and runs handleSessionExpired
    window.addEventListener("session-expired", handleSessionExpired);

    return () => {
      // Cleanup the component when app dismounts (avoids dup behavior)
      window.removeEventListener("session-expired", handleSessionExpired);
    };
  }, []);

  if (authLoading) {
    return (
      <main className="container">
        <LoadingState message="Checking your session..." />
      </main>
    );
  }

  function handleAuthSuccess(user) {
    setCurrentUser(user);
    setAuthError("");
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
          element={<LoginPage setCurrentUser={handleAuthSuccess} />}
        ></Route>
        <Route
          path="/register"
          element={<RegisterPage setCurrentUser={handleAuthSuccess} />}
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
