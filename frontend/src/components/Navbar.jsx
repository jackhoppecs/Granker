import { Link } from "react-router-dom";

function Navbar() {
  return (
    <nav className="navbar">
      <Link className="navbar-brand" to="/">
        Granker
      </Link>

      <div className="navbar-links">
        <Link to="/">Products</Link>
        <Link to="/products/new">Add Product</Link>
      </div>
    </nav>
  );
}

export default Navbar;
