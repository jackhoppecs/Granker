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
      console.error(err);
      setError("Registration failed. Please try again");
    }
  }

  return (
    <main className="page">
      <h1>Create Account</h1>

      {error && <p className="error-message">{error}</p>}

      <form onSubmit={handleSubmit} className="form-card">
        <label>
          Username
          <input
            name="username"
            value={formData.username}
            onChange={handleChange}
            required
          />
        </label>

        <label>
          Email
          <input
            name="email"
            type="email"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </label>

        <label>
          Password
          <input
            name="password"
            type="password"
            value={formData.password}
            onChange={handleChange}
            required
          />
        </label>

        <button type="submit">Register</button>
      </form>
    </main>
  );
}

export default RegisterPage;
