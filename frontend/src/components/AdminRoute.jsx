function AdminRoute({ currentUser, children }) {
  if (!currentUser) {
    return <p>You must be logged in to view this page.</p>;
  }

  if (!currentUser.admin) {
    return <p>You do not have permission to view this page.</p>;
  }

  return children;
}

export default AdminRoute;
