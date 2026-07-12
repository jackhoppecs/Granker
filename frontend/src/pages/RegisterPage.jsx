import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { register } from "../api/auth";

function RegisterPage({ setCurrentUser }) {
  const [formData, setFormData] = useState({
    username: "",
    email: "",
    password: "",
  });

  const [error, setError] = useState("");
  const navigate = useNavigate();

  function handleChange(e) {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");

    try {
      const user = await register(
        formData.username,
        formData.email,
        formData.password,
      );

      setCurrentUser(user);
      navigate("/products");
    } catch (err) {
      setError("Registration failed. Please try again");
    }
  }

  return (
    <main className="container">
      <header className="page-header">
        <h1>Create Account</h1>
      </header>

      {error && <p className="form-error">{error}</p>}

      <form onSubmit={handleSubmit} className="review-form">
        <div className="form-group">
          <label htmlFor="register-username">Username</label>
          <input
            id="register-username"
            name="username"
            value={formData.username}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="register-email">Email</label>
          <input
            id="register-email"
            name="email"
            type="email"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="register-password">Password</label>
          <input
            id="register-password"
            name="password"
            type="password"
            value={formData.password}
            onChange={handleChange}
            required
          />
        </div>

        <button className="primary-button" type="submit">
          Register
        </button>
      </form>
    </main>
  );
}

export default RegisterPage;
