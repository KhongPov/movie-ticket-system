import { NavLink, useNavigate } from "react-router-dom";
import { useAuthStore } from "../store/useAuthStore";
import "../styles/navbar.css";

export function Navbar() {
  const navigate = useNavigate();
  const { isAuthenticated, user, logout } = useAuthStore();

  const handleLogout = async () => {
    await logout();
    navigate("/");
  };

  const primaryLinks = [{ label: "Home", to: "/" }];
  const authedLinks = [{ label: "My Bookings", to: "/my-bookings" }];

  const getInitials = () => {
    if (user?.firstName) {
      return user.firstName.charAt(0).toUpperCase();
    }
    if (user?.email) {
      return user.email.charAt(0).toUpperCase();
    }
    return "U";
  };

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <button
          type="button"
          className="navbar-brand"
          onClick={() => navigate("/")}
        >
          <span className="brand-icon" aria-hidden="true">
            🎬
          </span>
          <span>MovieTick</span>
        </button>

        <div
          className="navbar-menu"
          role="navigation"
          aria-label="Primary navigation"
        >
          {primaryLinks.map((link) => (
            <NavLink
              key={link.to}
              to={link.to}
              className={({ isActive }) =>
                `nav-link${isActive ? " active" : ""}`
              }
            >
              {link.label}
            </NavLink>
          ))}

          {isAuthenticated &&
            authedLinks.map((link) => (
              <NavLink
                key={link.to}
                to={link.to}
                className={({ isActive }) =>
                  `nav-link${isActive ? " active" : ""}`
                }
              >
                {link.label}
              </NavLink>
            ))}
        </div>

        <div className="navbar-auth">
          {isAuthenticated ? (
            <>
              <div className="user-pill">
                <div className="avatar" aria-hidden="true">
                  {getInitials()}
                </div>
                <div className="user-details">
                  <span className="user-name">
                    {user?.firstName} {user?.lastName}
                  </span>
                  <span className="user-email">{user?.email}</span>
                </div>
              </div>
              <button
                type="button"
                onClick={handleLogout}
                className="navbar-btn solid"
              >
                Logout
              </button>
            </>
          ) : (
            <>
              <button
                type="button"
                className="navbar-btn ghost"
                onClick={() => navigate("/login")}
              >
                Login
              </button>
              <button
                type="button"
                className="navbar-btn solid"
                onClick={() => navigate("/register")}
              >
                Register
              </button>
            </>
          )}
        </div>
      </div>
    </nav>
  );
}
