import { useEffect, useState } from "react";
import { Alert, Button, Card, Form, Table, Badge } from "react-bootstrap";
import { Navigate } from "react-router-dom";
import { authApis, endpoints } from "../configs/Apis";

const LockCommittee = ({ currentUser }) => {
  const [list, setList] = useState(null);
  const [id, setId] = useState("");
  const [msg, setMsg] = useState(null);
  const [err, setErr] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const load = async () => {
      try {
        const res = await authApis().get(endpoints["committees-list"]);
        setList(Array.isArray(res.data) ? res.data : []);
      } catch (_) {
        setList(null);
      }
    };
    if (endpoints["committees-list"]) load();
  }, []);

  const lock = async (committeeId) => {
    setMsg(null); setErr(null);
    if (!committeeId) { setErr("Vui lòng nhập/chọn ID hội đồng."); return; }

    try {
      setLoading(true);
      const res = await authApis().put(endpoints["lock-committee"](committeeId));
      setMsg(res?.data || "Đã khoá hội đồng.");
      setList(prev => Array.isArray(prev)
        ? prev.map(c => c.id === Number(committeeId) ? { ...c, available: false } : c)
        : prev
      );
    } catch (e) {
      console.error(e);
      setErr(e?.response?.data || "Khoá hội đồng thất bại.");
    } finally {
      setLoading(false);
    }
  };

  if (!currentUser || !["ACADEMIC_STAFF", "ADMIN"].includes(currentUser.userRole))
    return <Navigate to="/login" replace />;

  return (
    <Card className="mt-3 shadow-sm">
      <Card.Body>
        <h3 className="text-primary mb-4">
          <i className="fas fa-lock me-2"></i>
          Quản Lý Hội Đồng Bảo Vệ
        </h3>
        
        {msg && <Alert variant="success" dismissible onClose={() => setMsg(null)}>
          <i className="fas fa-check-circle me-2"></i>{msg}
        </Alert>}
        {err && <Alert variant="danger" dismissible onClose={() => setErr(null)}>
          <i className="fas fa-exclamation-circle me-2"></i>{err}
        </Alert>}

        {list === null ? (
          <Form onSubmit={(e)=>{e.preventDefault(); lock(id);}} className="d-flex gap-2">
            <Form.Control
              placeholder="Nhập ID hội đồng"
              type="number"
              value={id}
              onChange={(e) => setId(e.target.value)}
              className="w-50"
            />
            <Button 
              type="submit" 
              disabled={loading}
              variant="primary"
            >
              {loading ? (
                <>
                  <span className="spinner-border spinner-border-sm me-2" />
                  Đang xử lý...
                </>
              ) : (
                <>
                  <i className="fas fa-lock me-2"></i>
                  Khoá hội đồng
                </>
              )}
            </Button>
          </Form>
        ) : (
          <Table bordered hover responsive className="mt-3 align-middle">
            <thead className="bg-primary text-white">
              <tr>
                <th className="text-center" style={{width: 80}}>ID</th>
                <th>Tên hội đồng</th>
                <th style={{width: 150}}>Trạng thái</th>
                <th className="text-center" style={{width: 120}}>Thao tác</th>
              </tr>
            </thead>
            <tbody>
              {list.length === 0 ? (
                <tr>
                  <td colSpan={4} className="text-center text-muted py-4">
                    <i className="fas fa-info-circle me-2"></i>
                    Chưa có hội đồng nào được tạo
                  </td>
                </tr>
              ) : list.map(c => (
                <tr key={c.id}>
                  <td className="text-center">{c.id}</td>
                  <td>{c.name}</td>
                  <td>
                    <Badge bg={c.available ? "success" : "secondary"}>
                      <i className={`fas fa-${c.available ? 'unlock' : 'lock'} me-1`}></i>
                      {c.available ? "Đang mở" : "Đã khoá"}
                    </Badge>
                  </td>
                  <td className="text-center">
                    <Button
                      size="sm"
                      variant={c.available ? "danger" : "secondary"}
                      disabled={!c.available || loading}
                      onClick={() => lock(c.id)}
                    >
                      {loading ? (
                        <span className="spinner-border spinner-border-sm" />
                      ) : (
                        <>
                          <i className={`fas fa-${c.available ? 'lock' : 'lock'} me-1`}></i>
                          {c.available ? "Khoá" : "Đã khoá"}
                        </>
                      )}
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        )}
      </Card.Body>
    </Card>
  );
};

export default LockCommittee;
