import { useContext, useEffect, useState } from "react";
import { MyUserContext } from "../configs/Contexts";
import { authApis, endpoints } from "../configs/Apis";
import { Table, Alert, Button, Card, Spinner } from "react-bootstrap";
import { Navigate } from "react-router-dom";

const UserList = () => {
    const [user] = useContext(MyUserContext);
    const [users, setUsers] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const loadUsers = async () => {
            try {
                setLoading(true);
                const res = await authApis().get(endpoints["users-list"]);
                setUsers(Array.isArray(res.data) ? res.data : []);
            } catch (err) {
                console.error(err);
                setError("Lỗi khi tải danh sách người dùng: " + (err.response?.data || err.message));
            } finally {
                setLoading(false);
            }
        };

        loadUsers();
    }, []);

    if (!user || user.userRole !== "ADMIN")
        return <Navigate to="/login" replace />

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
            <Card className="shadow-sm">
                <Card.Body>
                    <h2 className="text-primary mb-4">Danh sách người dùng</h2>

                    {error && (
                        <Alert variant="danger" dismissible onClose={() => setError(null)}>
                            {error}
                        </Alert>
                    )}

                    {loading ? (
                        <div className="text-center py-4">
                            <Spinner animation="border" variant="primary" />
                            <p className="mt-2 text-muted">Đang tải danh sách...</p>
                        </div>
                    ) : users.length === 0 ? (
                        <Alert variant="info">
                            Chưa có người dùng nào trong hệ thống.
                        </Alert>
                    ) : (
                        <Table bordered hover responsive className="align-middle">
                            <thead className="bg-light">
                                <tr>
                                    <th className="text-center" style={{width: "60px"}}>ID</th>
                                    <th>Họ tên</th>
                                    <th>Tên đăng nhập</th>
                                    <th>Email</th>
                                    <th style={{width: "150px"}}>Vai trò</th>
                                    <th className="text-center" style={{width: "100px"}}>Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                {users.map(u => (
                                    <tr key={u.id}>
                                        <td className="text-center">{u.id}</td>
                                        <td>{u.lastName} {u.firstName}</td>
                                        <td>{u.username}</td>
                                        <td>{u.email}</td>
                                        <td>
                                            <span className="badge bg-primary">
                                                {getRoleName(u.userRole)}
                                            </span>
                                        </td>
                                        <td className="text-center">
                                            <Button 
                                                variant="outline-warning" 
                                                size="sm"
                                                disabled
                                            >
                                                Chỉnh sửa
                                            </Button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </Table>
                    )}
                </Card.Body>
            </Card>
        </div>
    );
};

export default UserList;
