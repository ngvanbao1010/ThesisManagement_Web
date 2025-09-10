import { useEffect, useState } from "react";
import { Alert, Button, Form, Table, Card } from "react-bootstrap";
import { authApis, endpoints } from "../configs/Apis";

const CriteriaSetup = () => {
  const [criteria, setCriteria] = useState([]);
  const [newCriterion, setNewCriterion] = useState("");
  const [msg, setMsg] = useState(null);
  const [err, setErr] = useState(null);
  const [loading, setLoading] = useState(false);

  const load = async () => {
    try {
      setLoading(true);
      const res = await authApis().get(endpoints["criteria"]);
      setCriteria(res.data || []);
    } catch (e) {
      setErr(e.response?.data || "Không tải được danh sách tiêu chí.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  const add = async () => {
    setMsg(null); 
    setErr(null);
    if (!newCriterion.trim()) return;

    try {
      setLoading(true);
      await authApis().post(endpoints["criteria"], { criterion: newCriterion.trim() });
      setMsg("Đã thêm tiêu chí thành công!");
      setNewCriterion("");
      await load();
    } catch (e) {
      setErr(e.response?.data || "Không thêm được tiêu chí.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mt-4">
      <Card className="shadow-sm">
        <Card.Body>
          <h2 className="text-primary mb-4">Thiết Lập Tiêu Chí Đánh Giá</h2>

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

          <Card className="bg-light border-0 mb-4">
            <Card.Body>
              <div className="d-flex gap-2">
                <Form.Control
                  placeholder="Nhập tiêu chí mới..."
                  value={newCriterion}
                  onChange={(e) => setNewCriterion(e.target.value)}
                  disabled={loading}
                  className="py-2"
                />
                <Button 
                  onClick={add} 
                  disabled={loading || !newCriterion.trim()}
                  variant="primary"
                  className="px-4"
                >
                  {loading ? (
                    <>
                      <span className="spinner-border spinner-border-sm me-2" />
                      Đang xử lý...
                    </>
                  ) : "Thêm"}
                </Button>
              </div>
            </Card.Body>
          </Card>

          <Table bordered hover responsive className="align-middle">
            <thead className="bg-light">
              <tr>
                <th className="text-center" style={{width: "80px"}}>ID</th>
                <th>Tiêu chí</th>
              </tr>
            </thead>
            <tbody>
              {loading ? (
                <tr>
                  <td colSpan={2} className="text-center py-4">
                    <div className="spinner-border text-primary" />
                    <p className="text-muted mt-2 mb-0">Đang tải dữ liệu...</p>
                  </td>
                </tr>
              ) : criteria.length === 0 ? (
                <tr>
                  <td colSpan={2} className="text-center py-4 text-muted">
                    Chưa có tiêu chí nào được thiết lập.
                  </td>
                </tr>
              ) : criteria.map(c => (
                <tr key={c.id}>
                  <td className="text-center">{c.id}</td>
                  <td>{c.criterion || c.name}</td>
                </tr>
              ))}
            </tbody>
          </Table>
        </Card.Body>
      </Card>
    </div>
  );
};

export default CriteriaSetup;
