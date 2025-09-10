import { useState, useEffect } from "react";
import { authApis, endpoints } from "../configs/Apis";
import { Card, Button, Alert, Row, Col, Image } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const Profile = () => {
    const [user, setUser] = useState(null);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchUserProfile = async () => {
            try {
                const res = await authApis().get(endpoints['profile']); 
                setUser(res.data);  
            } catch (err) {
                setError("Không thể tải thông tin hồ sơ");
                console.error(err);
            }
        };

        fetchUserProfile();
    }, []);

    if (!user) {
        return (
            <div className="container mt-4 text-center">
                {error ? (
                    <Alert variant="danger" className="shadow-sm">{error}</Alert>
                ) : (
                    <div className="spinner-border text-primary" role="status">
                        <span className="visually-hidden">Đang tải...</span>
                    </div>
                )}
            </div>
        );
    }

    const getRoleName = (role) => {
        const roles = {
            ADMIN: "Quản trị viên",
            LECTURER: "Giảng viên",
            STUDENT: "Sinh viên",
            ACADEMIC_STAFF: "Giáo vụ"
        };
        return roles[role] || role;
    };

    return (
        <div className="container mt-4">
            <Card className="shadow">
                <Card.Body>
                    <Row>
                        <Col md={4} className="text-center mb-4 mb-md-0">
                            <div className="position-relative d-inline-block">
                                <Image
                                    src={user.avatar || "https://via.placeholder.com/150"}
                                    alt="Avatar"
                                    roundedCircle
                                    className="shadow"
                                    style={{ width: '150px', height: '150px', objectFit: 'cover' }}
                                />
                            </div>
                            <div className="mt-3">
                                <h4 className="mb-1">{user.firstName} {user.lastName}</h4>
                                <span className="badge bg-primary">{getRoleName(user.userRole)}</span>
                            </div>
                        </Col>
                        
                        <Col md={8}>
                            <h3 className="text-primary mb-4">Thông tin cá nhân</h3>
                            <Row className="g-3">
                                <Col sm={6}>
                                    <div className="mb-3">
                                        <label className="text-muted small">Tên đăng nhập</label>
                                        <p className="fw-medium">{user.username}</p>
                                    </div>
                                </Col>
                                <Col sm={6}>
                                    <div className="mb-3">
                                        <label className="text-muted small">Email</label>
                                        <p className="fw-medium">{user.email}</p>
                                    </div>
                                </Col>
                                <Col sm={6}>
                                    <div className="mb-3">
                                        <label className="text-muted small">Số điện thoại</label>
                                        <p className="fw-medium">{user.phone || "Chưa cập nhật"}</p>
                                    </div>
                                </Col>
                                <Col sm={6}>
                                    <div className="mb-3">
                                        <label className="text-muted small">Ngày tạo tài khoản</label>
                                        <p className="fw-medium">
                                            {new Date(user.createdDate).toLocaleDateString('vi-VN')}
                                        </p>
                                    </div>
                                </Col>
                            </Row>

                            <div className="mt-4 text-end">
                                <Button 
                                    variant="outline-secondary"
                                    onClick={() => navigate("/change-password")}
                                >
                                    Đổi mật khẩu
                                </Button>
                            </div>
                        </Col>
                    </Row>
                </Card.Body>
            </Card>
        </div>
    );
};

export default Profile;
