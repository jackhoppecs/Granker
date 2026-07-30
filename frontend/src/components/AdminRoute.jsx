import { Navigate, useLocation } from "react-router-dom";

function AdminRoute({ currentUser, children }) {
  const location = useLocation();

  if (!currentUser) {
    return (
      <Navigate
        to="/login"
        replace
        state={{
          from: location.pathname,
          message: "Your session has expired. Please log in again.",
        }}
      />
    );
  }

  if (!currentUser.admin) {
    return (
      <main className="container">
        <div className="error-state">
          <h1>Access denied</h1>
          <p>You do not have permission to view this page.</p>
        </div>
      </main>
    );
  }

  return children;
}

export default AdminRoute;
