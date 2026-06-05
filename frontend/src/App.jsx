import { BrowserRouter, Routes, Route } from "react-router-dom";
import ProductsPage from "./pages/ProductsPage";
import ProductDetailsPage from "./pages/ProductDetailsPage";
import Navbar from "./components/Navbar";

function App() {
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
        <Route path="/" element={<ProductsPage />}></Route>
        <Route path="/products" element={<ProductsPage />}></Route>
        <Route path="/products/:id" element={<ProductDetailsPage />}></Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
