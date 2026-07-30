import { Navigate, useLocation } from "react-router-dom";

function ProtectedRoute({ currentUser, sessionExpired, children }) {
  const location = useLocation();

  if (!currentUser) {
    return (
      <Navigate
        to="/login"
        replace
        state={{
          from: location.pathname,
          message: sessionExpired
            ? "Your session has expired. Please log in again."
            : "Please log in to continue.",
        }}
      />
    );
  }

  return children;
}

export default ProtectedRoute;
