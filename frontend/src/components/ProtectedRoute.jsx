import { Navigate, useLocation } from "react-router-dom";

function ProtectedRoute({ currentUser, children }) {
  const location = useLocation();

  if (!currentUser) {
    return (
      <Navigate
        to="/login"
        replace
        state={{
          from: location.pathname,
          message: "Please log in to continue.",
        }}
      />
    );
  }

  return children;
}

export default ProtectedRoute;
