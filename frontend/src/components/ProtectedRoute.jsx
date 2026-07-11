function ProtectedRoute({ currentUser, children }) {
  if (!currentUser) {
    return <p>You must be logged in to view this page.</p>;
  }

  return children;
}

export default ProtectedRoute;
