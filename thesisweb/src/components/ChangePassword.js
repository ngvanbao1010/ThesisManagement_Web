import { useContext, useState } from "react";
import { Alert, Button, Form, Card, Container, Row, Col } from "react-bootstrap";
import { MyUserContext } from "../configs/Contexts";
import { authApis, endpoints } from "../configs/Apis";

const ChangePassword = () => {
  const [user] = useContext(MyUserContext);
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [msg, setMsg] = useState(null);
  const [err, setErr] = useState(null);
  const [loading, setLoading] = useState(false);

  const submit = async (e) => {
    e.preventDefault();
    setErr(null); 
    setMsg(null);

    if (!user) {
      setErr("Bạn cần đăng nhập để đổi mật khẩu!");
      return;
    }

    try {
      setLoading(true);
      const res = await authApis().post(
        endpoints['change-password'],
        null,
        {
          params: {
            'old-password': oldPassword,
            'new-password': newPassword
          }
        }
      );
      if (res.status === 200) {
        setMsg("Đổi mật khẩu thành công!");
        setOldPassword(""); 
        setNewPassword("");
      }
    } catch (ex) {
      setErr(ex?.response?.data || "Mật khẩu cũ không đúng hoặc lỗi hệ thống.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container className="mt-4">
      <Row className="justify-content-center">
        <Col md={6} lg={5}>
          <Card className="shadow border-0">
            <Card.Body className="p-4">
              <h2 className="text-primary text-center mb-4">Đổi Mật Khẩu</h2>

              {msg && (
                <Alert variant="success" dismissible onClose={() => setMsg(null)}>
                  {msg}
                </Alert>
              )}
              
              {err && (
                <Alert variant="danger" dismissible onClose={() => setErr(null)}>
                  {err}
                </Alert>
              )}

              <Form onSubmit={submit}>
                <Form.Group className="mb-3">
                  <Form.Label className="fw-medium">Mật khẩu hiện tại</Form.Label>
                  <Form.Control
                    type="password"
                    required
                    value={oldPassword}
                    onChange={e => setOldPassword(e.target.value)}
                    placeholder="Nhập mật khẩu hiện tại"
                    className="py-2"
                  />
                </Form.Group>

                <Form.Group className="mb-4">
                  <Form.Label className="fw-medium">Mật khẩu mới</Form.Label>
                  <Form.Control
                    type="password"
                    required
                    value={newPassword}
                    onChange={e => setNewPassword(e.target.value)}
                    placeholder="Nhập mật khẩu mới"
                    className="py-2"
                  />
                </Form.Group>

                <div className="d-grid">
                  <Button 
                    type="submit" 
                    variant="primary" 
                    disabled={loading}
                    size="lg"
                    className="py-2"
                  >
                    {loading ? (
                      <>
                        <span className="spinner-border spinner-border-sm me-2" />
                        Đang xử lý...
                      </>
                    ) : "Đổi mật khẩu"}
                  </Button>
                </div>
              </Form>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default ChangePassword;
