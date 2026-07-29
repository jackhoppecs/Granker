import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../api/auth";

function LoginPage({ setCurrentUser }) {
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [formError, setFormError] = useState("");

  async function handleSubmit(event) {
    setFormError("");
    event.preventDefault();

    const trimmedEmail = email.trim();

    if (!trimmedEmail || !password.trim()) {
      setFormError("Email and password are required.");
      return;
    }

    if (!trimmedEmail.includes("@")) {
      setFormError("Enter a valid email address.");
      return;
    }

    try {
      setSubmitting(true);
      setFormError("");
      const user = await login({
        email: trimmedEmail,
        password,
      });

      setCurrentUser(user);
      navigate("/products");
    } catch (err) {
      setFormError(err.message || "Login failed.");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <main className="container">
      <header className="page-header">
        <h1>Log In</h1>
        <p>Log in to submit reviews.</p>
      </header>
      <form className="review-form" onSubmit={handleSubmit} noValidate>
        {formError && (
          <p className="form-error" role="alert">
            {formError}
          </p>
        )}

        <div className="form-group">
          <label htmlFor="login-email">Email</label>
          <input
            id="login-email"
            type="email"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
            disabled={submitting}
            autoComplete="username"
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="login-password">Password</label>
          <input
            id="login-password"
            type="password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            disabled={submitting}
            autoComplete="current-password"
            required
          />
        </div>

        <button className="primary-button" type="submit" disabled={submitting}>
          {submitting ? "Logging in..." : "Log In"}
        </button>
      </form>
    </main>
  );
}

export default LoginPage;
