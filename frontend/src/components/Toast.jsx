import { useEffect } from "react";

function Toast({ message, type, onClose }) {
  useEffect(() => {
    const timeoutId = setTimeout(() => {
      onClose();
    }, 5000);

    return () => clearTimeout(timeoutId);
  }, [message, onClose]);

  return (
    <div className={`toast toast-${type}`} role="status">
      <span>{message}</span>

      <button
        type="button"
        className="toast-button"
        onClick={onClose}
        aria-label="Dismiss notification"
      >
        X
      </button>
    </div>
  );
}

export default Toast;
