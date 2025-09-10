import { MyUserContext } from "../configs/Contexts";
import { useContext } from "react";
import { Navigate, Link } from "react-router-dom";
import { Card, Row, Col } from "react-bootstrap";

const AdminDashboard = () => {
  const [user] = useContext(MyUserContext);

  if (!user || user.userRole !== "ADMIN") return <Navigate to="/login" replace />;

  return (
    <div className="container mt-4">
      <Card className="shadow-sm mb-4">
        <Card.Body>
          <h2 className="text-primary mb-3">Chào mừng Admin: {user.lastName} {user.firstName}</h2>
          <p className="text-muted mb-0">Chức năng quản lý người dùng</p>
        </Card.Body>
      </Card>

      <Row className="g-4">
        <Col md={6}>
          <Card className="h-100 shadow-sm">
            <Card.Body className="d-flex flex-column">
              <Card.Title>Tạo tài khoản</Card.Title>
              <Card.Text>Tạo tài khoản mới cho người dùng trong hệ thống</Card.Text>
              <Link to="/admin/create-user" className="mt-auto">
                <button className="btn btn-primary w-100">Tạo tài khoản</button>
              </Link>
            </Card.Body>
          </Card>
        </Col>

        <Col md={6}>
          <Card className="h-100 shadow-sm">
            <Card.Body className="d-flex flex-column">
              <Card.Title>Danh sách người dùng</Card.Title>
              <Card.Text>Xem và quản lý tất cả người dùng trong hệ thống</Card.Text>
              <Link to="/admin/users" className="mt-auto">
                <button className="btn btn-outline-primary w-100">Xem danh sách</button>
              </Link>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default AdminDashboard;
