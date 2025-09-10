import { useEffect, useState } from "react";
import { Alert, Button, Form, Card, Row, Col } from "react-bootstrap";
import { authApis, endpoints } from "../../configs/Apis";

const StudentHome = () => {
    const [myThesis, setMyThesis] = useState(null);
    const [fileUrl, setFileUrl] = useState("");
    const [msg, setMsg] = useState(null);
    const [err, setErr] = useState(null);
    const [loading, setLoading] = useState(false);

    const loadMyThesis = async () => {
        try {
            setLoading(true);
            const res = await authApis().get(endpoints["my-thesis"]);
            setMyThesis(res.data);
        } catch (e) {
            setErr("Không tải được thông tin khóa luận: " + (e.response?.data || e.message));
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadMyThesis();
    }, []);

    const validateSubmission = () => {
        if (!myThesis) {
            setErr("Không tìm thấy thông tin khóa luận!");
            return false;
        }
        if (!fileUrl.trim()) {
            setErr("Vui lòng nhập link file khóa luận!");
            return false;
        }
        try {
            new URL(fileUrl);
        } catch {
            setErr("Link file không hợp lệ!");
            return false;
        }
        return true;
    };

    const submitLink = async (e) => {
        e.preventDefault();
        setMsg(null);
        setErr(null);

        if (!validateSubmission()) return;

        try {
            setLoading(true);
            const res = await authApis().post(endpoints["student-submit"], 
                { fileUrl: fileUrl.trim() },
                { headers: { 'Content-Type': 'application/json' }}
            );

            if (res.data) {
                setMsg("Nộp khóa luận thành công!");
                setFileUrl("");
                await loadMyThesis();
            } else {
                throw new Error("Không nhận được phản hồi từ server");
            }
        } catch (e) {
            setErr(e.response?.data || e.message || "Lỗi khi nộp khóa luận!");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container mt-4">
            <Card className="shadow-sm mb-4">
                <Card.Body>
                    <h2 className="text-primary mb-3">Thông Tin Sinh Viên</h2>
                    <p className="text-muted mb-0">Quản lý và nộp khóa luận tốt nghiệp</p>
                </Card.Body>
            </Card>

            {msg && <Alert variant="success" dismissible onClose={() => setMsg(null)}>{msg}</Alert>}
            {err && <Alert variant="danger" dismissible onClose={() => setErr(null)}>{err}</Alert>}

            <Card className="shadow-sm">
                <Card.Body>
                    <Card.Title className="border-bottom pb-3 mb-4">Chi Tiết Khóa Luận</Card.Title>
                    {myThesis ? (
                        <Row>
                            <Col lg={8}>
                                <div className="mb-4">
                                    <Row className="mb-3">
                                        <Col sm={4} className="text-muted">Tên đề tài:</Col>
                                        <Col sm={8} className="fw-medium">{myThesis.title}</Col>
                                    </Row>
                                    <Row className="mb-3">
                                        <Col sm={4} className="text-muted">Mã số:</Col>
                                        <Col sm={8} className="fw-medium">{myThesis.id}</Col>
                                    </Row>
                                    <Row className="mb-3">
                                        <Col sm={4} className="text-muted">Trạng thái:</Col>
                                        <Col sm={8}>
                                            <span className={`badge bg-${myThesis.status === 'Đã nộp' ? 'success' : 'warning'}`}>
                                                {myThesis.status}
                                            </span>
                                        </Col>
                                    </Row>
                                    {myThesis.fileUrl && (
                                        <Row className="mb-3">
                                            <Col sm={4} className="text-muted">File đã nộp:</Col>
                                            <Col sm={8}>
                                                <a href={myThesis.fileUrl} 
                                                   target="_blank" 
                                                   rel="noopener noreferrer"
                                                   className="btn btn-outline-primary btn-sm">
                                                    Xem file đã nộp
                                                </a>
                                            </Col>
                                        </Row>
                                    )}
                                </div>

                                <Form onSubmit={submitLink} className="border-top pt-4">
                                    <Form.Group className="mb-3">
                                        <Form.Label className="fw-medium">Nộp/Cập nhật file khóa luận</Form.Label>
                                        <Form.Control
                                            type="url"
                                            placeholder="Nhập link Google Drive/OneDrive..."
                                            value={fileUrl}
                                            onChange={(e) => setFileUrl(e.target.value)}
                                            disabled={loading}
                                            className="mb-2"
                                        />
                                        <Form.Text className="text-muted">
                                            Link phải được chia sẻ công khai
                                        </Form.Text>
                                    </Form.Group>
                                    <Button 
                                        type="submit" 
                                        variant="primary"
                                        disabled={loading}
                                        className="px-4"
                                    >
                                        {loading ? (
                                            <>
                                                <span className="spinner-border spinner-border-sm me-2" />
                                                Đang xử lý...
                                            </>
                                        ) : "Nộp khóa luận"}
                                    </Button>
                                </Form>
                            </Col>
                        </Row>
                    ) : (
                        <div className="text-center py-4">
                            {loading ? (
                                <>
                                    <div className="spinner-border text-primary mb-2" />
                                    <p className="text-muted mb-0">Đang tải thông tin...</p>
                                </>
                            ) : (
                                <p className="text-muted mb-0">Chưa có thông tin khóa luận</p>
                            )}
                        </div>
                    )}
                </Card.Body>
            </Card>
        </div>
    );
};

export default StudentHome;
