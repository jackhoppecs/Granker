import { BrowserRouter, Routes, Route } from "react-router-dom";
import { useState } from "react";
import ProductsPage from "./pages/ProductsPage";
import ProductDetailsPage from "./pages/ProductDetailsPage";
import CreateProductPage from "./pages/CreateProductPage";
import LoginPage from "./pages/LoginPage";
import Navbar from "./components/Navbar";

function App() {
  const [currentUser, setCurrentUser] = useState(null);
  return (
    // BrowserRouter connects your React app to the Browser's URL bar
    // React router can look at the URL and decide what component to display
    // Need for Link to = and path = to work

    // this is basically the main class of the app
    // Rotutes decides which page to show based on url
    // Navbar is everywhere because it's outside of routes
    <BrowserRouter>
      <Navbar />
      <Routes>
        {/* These are just paths to where we have these pages not same as APIs */}
        <Route path="/" element={<ProductsPage />}></Route>
        <Route path="/products" element={<ProductsPage />}></Route>
        {/* This needs to go before Product details route so it doesnt think 'new' is a product id */}
        <Route path="/products/new" element={<CreateProductPage />}></Route>
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
      </Routes>
    </BrowserRouter>
  );
}

export default App;
