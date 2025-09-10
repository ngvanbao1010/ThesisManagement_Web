import { Link, Navigate } from "react-router-dom";
import { Button, Card, Row, Col } from "react-bootstrap";
import { useContext } from "react";
import { MyUserContext } from "../../configs/Contexts";

const AcademicStaffHome = () => {
  const [user] = useContext(MyUserContext);
  if (!user || user.userRole !== "ACADEMIC_STAFF") return <Navigate to="/login" replace />;

  return (
    <div className="container mt-4">
      <Card className="shadow-sm mb-4">
        <Card.Body>
          <h2 className="text-primary mb-3">Chào mừng Giáo vụ: {user.lastName} {user.firstName}</h2>
          <p className="text-muted">Chức năng quản lý khoá luận & hội đồng</p>
        </Card.Body>
      </Card>

      <Row className="g-4">
        <Col md={6}>
          <Card className="h-100 shadow-sm">
            <Card.Body className="d-flex flex-column">
              <Card.Title>Quản lý khóa luận</Card.Title>
              <div className="d-grid gap-2 mt-3">
                <Link to="/theses">
                  <Button variant="primary" className="w-100">Tạo khóa luận</Button>
                </Link>
                <Link to="/committees">
                  <Button variant="secondary" className="w-100">Tạo hội đồng</Button>
                </Link>
              </div>
            </Card.Body>
          </Card>
        </Col>

        <Col md={6}>
          <Card className="h-100 shadow-sm">
            <Card.Body className="d-flex flex-column">
              <Card.Title>Quản lý hội đồng</Card.Title>
              <div className="d-grid gap-2 mt-3">
                <Link to="/academic_staff/criteria">
                  <Button variant="outline-primary" className="w-100">Thiết lập tiêu chí chấm</Button>
                </Link>
                <Link to="/academic_staff/lock-committee">
                  <Button variant="outline-danger" className="w-100">Khoá hội đồng</Button>
                </Link>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default AcademicStaffHome;
