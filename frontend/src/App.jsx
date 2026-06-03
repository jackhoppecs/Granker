import { BrowserRouter, Routes, Route } from "react-router-dom";
import ProductsPage from "./pages/ProductsPage";
import ProductDetailsPage from "./pages/ProductDetailsPage";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<ProductsPage />}></Route>
        <Route path="/products" element={<ProductsPage />}></Route>
        <Route path="/products/:id" element={<ProductDetailsPage />}></Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
