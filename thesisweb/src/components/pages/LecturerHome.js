import { useContext } from "react";
import { MyUserContext } from "../../configs/Contexts";
import { Navigate, Link } from "react-router-dom";
import { Card, Row, Col, Button } from "react-bootstrap";

const LecturerHome = () => {
  const [user] = useContext(MyUserContext);
  if (!user || user.userRole !== "LECTURER") return <Navigate to="/login" replace />;

  return (
    <div className="container mt-4">
      <Card className="shadow-sm mb-4">
        <Card.Body>
          <h2 className="text-primary mb-3">Chào mừng Giảng viên: {user.lastName} {user.firstName}</h2>
          <p className="text-muted">Chức năng quản lý khóa luận</p>
        </Card.Body>
      </Card>

      <Row className="justify-content-center">
        <Col md={6}>
          <Card className="shadow-sm h-100">
            <Card.Body className="d-flex flex-column">
              <Card.Title>Quản lý khóa luận</Card.Title>
              <Card.Text>
                Xem danh sách và chấm điểm các khóa luận được phân công hướng dẫn
              </Card.Text>
              <div className="mt-auto">
                <Link to="/lecturer/theses" className="text-decoration-none">
                  <Button variant="primary" className="w-100">
                    Danh sách khóa luận
                  </Button>
                </Link>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default LecturerHome;
