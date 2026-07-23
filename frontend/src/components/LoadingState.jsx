function LoadingState({ message = "Loading..." }) {
  return (
    // role = status and aria-live = polite help screen readers announce that the page is loading
    <div className="loading-state" role="status" aria-live="polite">
      <div className="loading-spinner" aria-hidden="true"></div>
      <p>{message}</p>
    </div>
  );
}

export default LoadingState;
