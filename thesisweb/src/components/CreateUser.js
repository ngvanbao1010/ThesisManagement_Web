import { useContext, useRef, useState } from "react";
import { Alert, Button, Form, Card, Row, Col } from "react-bootstrap";
import { authApis, endpoints } from "../configs/Apis";
import { MyUserContext } from "../configs/Contexts";

const AdminCreateUser = () => {
    const avatar = useRef();
    const [user, setUser] = useState({
        role: "STUDENT",
        password: "",
        confirm: ""
    });
    const [msg, setMsg] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);
    const [currentUser] = useContext(MyUserContext);

    const info = [
        { title: "Tên", field: "firstName", type: "text" },
        { title: "Họ và tên lót", field: "lastName", type: "text" },
        { title: "Số điện thoại", field: "phone", type: "tel" },
        { title: "Email", field: "email", type: "email" },
        { title: "Tên đăng nhập", field: "username", type: "text" },
        { title: "Mật khẩu", field: "password", type: "password" },
        { title: "Xác nhận mật khẩu", field: "confirm", type: "password" }
    ];

    const register = async (event) => {
        event.preventDefault();

        if (!currentUser || currentUser.userRole !== "ADMIN") {
            setError("Bạn không có quyền thực hiện chức năng này!");
            return;
        }

        if (user.password !== user.confirm) {
            setError("Mật khẩu không khớp!");
            return;
        }

        try {
            setLoading(true);
            setError(null);
            setMsg(null);

            let formData = new FormData();
            for (let key in user)
                if (key !== "confirm")
                    formData.append(key, user[key]);

            if (avatar.current?.files?.length > 0)
                formData.append("avatar", avatar.current.files[0]);

            let res = await authApis().post(endpoints['create-user'], formData);
            if (res.status === 201) {
                setMsg("Tạo tài khoản thành công!");
                setUser({ role: "STUDENT", password: "", confirm: "" });
                avatar.current.value = null;
            }
        } catch (ex) {
            setError("Lỗi khi tạo tài khoản.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container mt-4">
            <Card className="shadow-sm">
                <Card.Body>
                    <h2 className="text-primary text-center mb-4">Tạo Tài Khoản Người Dùng</h2>

                    {msg && <Alert variant="success" dismissible onClose={() => setMsg(null)}>{msg}</Alert>}
                    {error && <Alert variant="danger" dismissible onClose={() => setError(null)}>{error}</Alert>}

                    <Form onSubmit={register}>
                        <Row className="g-3">
                            {info.map(i => (
                                <Col md={6} key={i.field}>
                                    <Form.Group controlId={i.field}>
                                        <Form.Label>{i.title}</Form.Label>
                                        <Form.Control
                                            required
                                            value={user[i.field] || ""}
                                            onChange={e => setUser({ ...user, [i.field]: e.target.value })}
                                            type={i.type}
                                            placeholder={i.title}
                                        />
                                    </Form.Group>
                                </Col>
                            ))}

                            <Col md={6}>
                                <Form.Group>
                                    <Form.Label>Vai trò</Form.Label>
                                    <Form.Select 
                                        value={user.role} 
                                        onChange={e => setUser({ ...user, role: e.target.value })}
                                        className="form-select"
                                    >
                                        <option value="STUDENT">Sinh viên</option>
                                        <option value="LECTURER">Giảng viên</option>
                                        <option value="ACADEMIC_STAFF">Giáo vụ</option>
                                    </Form.Select>
                                </Form.Group>
                            </Col>

                            <Col md={6}>
                                <Form.Group controlId="avatar">
                                    <Form.Label>Ảnh đại diện</Form.Label>
                                    <Form.Control 
                                        type="file" 
                                        ref={avatar} 
                                        required 
                                        accept="image/*"
                                    />
                                </Form.Group>
                            </Col>
                        </Row>

                        <div className="text-center mt-4">
                            <Button 
                                type="submit" 
                                variant="primary" 
                                size="lg"
                                disabled={loading}
                                className="px-5"
                            >
                                {loading ? "Đang xử lý..." : "Tạo tài khoản"}
                            </Button>
                        </div>
                    </Form>
                </Card.Body>
            </Card>
        </div>
    );
};

export default AdminCreateUser;
