import { Link } from "react-router-dom";

function NotFoundPage() {
  return (
    <main className="container">
      <div className="empty-state">
        <h1>Page not found</h1>
        <p>The page you were looking for does not exist.</p>
        <Link to="/">Browse Products</Link>
      </div>
    </main>
  );
}

export default NotFoundPage;
