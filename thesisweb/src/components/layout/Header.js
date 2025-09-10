import { useContext } from "react";
import { Navbar, Nav, Container, Button, NavDropdown } from "react-bootstrap";
import { MyUserContext } from "../../configs/Contexts";
import { Link, useNavigate } from "react-router-dom";

const Header = () => {
    const [user, dispatch] = useContext(MyUserContext);  
    const navigate = useNavigate(); 

    const handleLogout = () => {
        dispatch({ type: "logout" }); 
        navigate("/login");  
    };

    const navigateToHome = () => {
        if (user) {
            const role = user.userRole;
            if (role === "ADMIN") navigate("/admin");
            else if (role === "ACADEMIC_STAFF") navigate("/academic_staff");
            else if (role === "LECTURER") navigate("/lecturer");
            else if (role === "STUDENT") navigate("/student");
        } else {
            navigate("/login"); 
        }
    };

    return (
        <Navbar bg="primary" variant="dark" expand="lg" className="shadow-sm">
            <Container>
                <Navbar.Brand 
                    onClick={navigateToHome} 
                    className="fw-bold" 
                    style={{ cursor: "pointer" }}
                >
                    Hệ thống Quản lý Khóa Luận
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="ms-auto">
                        {user ? (
                            <NavDropdown 
                                title={
                                    <span className="text-white">
                                        Xin chào, {user.lastName} {user.firstName}
                                    </span>
                                } 
                                id="basic-nav-dropdown"
                                align="end"
                                className="fw-medium"
                            >
                                <NavDropdown.Item as={Link} to="/profile">
                                    Thông tin cá nhân
                                </NavDropdown.Item>
                                <NavDropdown.Divider />
                                <NavDropdown.Item 
                                    onClick={handleLogout}
                                    className="text-danger"
                                >
                                    Đăng xuất
                                </NavDropdown.Item>
                            </NavDropdown>
                        ) : (
                            <Button 
                                variant="outline-light" 
                                as={Link} 
                                to="/login"
                                className="fw-medium"
                            >
                                Đăng nhập
                            </Button>
                        )}
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
};

export default Header;
