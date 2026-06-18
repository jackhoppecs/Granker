import { Link } from "react-router-dom";

// Name of props passed in MUST MATCH. This is because props are objects being passed in React
function Navbar({ currentUser, handleLogout }) {
  return (
    <nav className="navbar">
      <Link className="navbar-brand" to="/">
        Granker
      </Link>

      <div className="navbar-links">
        <Link to="/">Products</Link>
        <Link to="/products/new">Add Product</Link>
        {/* State changes cause components to re render so when currentuser changes from logout this conditional statement
        under app.jsx reruns since navbar is inside app.jsx. It's like a nested strucutre */}
        {currentUser ? (
          <button className="navbar-button" onClick={handleLogout}>
            Logout
          </button>
        ) : (
          <>
            <Link to="/login">Login</Link>
            <Link to="/register">Register</Link>
          </>
        )}
      </div>
    </nav>
  );
}

export default Navbar;
