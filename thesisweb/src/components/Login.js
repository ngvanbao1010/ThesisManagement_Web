import { useContext, useState } from "react";
import { Button, Form, Alert, Container, Row, Col } from "react-bootstrap";
import Apis, { authApis, endpoints } from "../configs/Apis";
import cookie from 'react-cookies';
import { MyUserContext } from "../configs/Contexts";
import { useNavigate } from "react-router-dom";

const Login = () => {
    const [, dispatch] = useContext(MyUserContext);
    const [user, setUser] = useState({});
    const [err, setErr] = useState(null);
    const nav = useNavigate();

    const login = async (e) => {
        e.preventDefault();
        setErr(null);

        try {
            const res = await Apis.post(endpoints['login'], user);
            cookie.save('token', res.data.token);

            const u = await authApis().get(endpoints['profile']);
            dispatch({
                type: 'login',
                payload: u.data
            });
            const role = u.data.userRole;
            if (role === "ADMIN")
                nav("/admin");
            else if (role === "ACADEMIC_STAFF")
                nav("/academic_staff");
            else if (role === "LECTURER")
                nav("/lecturer");
            else if (role === "STUDENT")
                nav("/student");
            else
                nav("/");
        } catch (error) {
            console.error(error);
            setErr("Sai tài khoản hoặc mật khẩu!");
        }
    };

    return (
        <div style={{
            flex: 1, 
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            background: "#f5f5f5"
        }}>
            <Container>
                <Row>
                    <Col xs={12} md={6} lg={4} className="mx-auto">
                        <h1 className="text-center text-success mb-4">Hệ thống Quản lý Khóa Luận OU</h1>
                        {err && <Alert variant="danger">{err}</Alert>}
                        <Form onSubmit={login} className="p-4 bg-white rounded shadow-sm">
                            <Form.Group className="mb-3">
                                <Form.Label>Tên đăng nhập</Form.Label>
                                <Form.Control
                                    required
                                    type="text"
                                    value={user.username || ""}
                                    onChange={e => setUser({ ...user, username: e.target.value })}
                                    placeholder="Nhập tên đăng nhập"
                                />
                            </Form.Group>
                            <Form.Group className="mb-3">
                                <Form.Label>Mật khẩu</Form.Label>
                                <Form.Control
                                    required
                                    type="password"
                                    value={user.password || ""}
                                    onChange={e => setUser({ ...user, password: e.target.value })}
                                    placeholder="Nhập mật khẩu"
                                />
                            </Form.Group>
                            <Button type="submit" variant="success" className="w-100">Đăng nhập</Button>
                        </Form>
                    </Col>
                </Row>
            </Container>
        </div>
    );
};

export default Login;
