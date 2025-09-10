import { useEffect, useMemo, useState } from "react";
import { Alert, Button, Card, Col, Form, Row } from "react-bootstrap";
import Select from "react-select";
import { authApis, endpoints } from "../configs/Apis";

const ROLE_PREVIEW = ["Chủ tịch", "Thư ký", "Phản biện", "Thành viên", "Thành viên"];

const CreateCommittee = () => {
  const [name, setName] = useState("");
  const [defenseDate, setDefenseDate] = useState("");
  const [theses, setTheses] = useState([]);
  const [selectedTheses, setSelectedTheses] = useState([]);
  const [lecturers, setLecturers] = useState([]);
  const [selectedLecturers, setSelectedLecturers] = useState([]);
  const [msg, setMsg] = useState(null);
  const [err, setErr] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const loadTheses = async () => {
      try {
        const res = await authApis().get(endpoints["theses"]);
        setTheses(res.data || []);
      } catch (ex) {
        setErr("Lỗi khi tải danh sách khoá luận.");
      }
    };
    loadTheses();
  }, []);

  const thesisOptions = useMemo(
    () => (theses || []).map((t) => ({
      value: t.id,
      label: t.title || `Thesis #${t.id}`,
    })),
    [theses]
  );

  useEffect(() => {
    const fetchAllowedLecturers = async () => {
      setErr(null);
      setLecturers([]);
      setSelectedLecturers([]);

      const thesisIds = selectedTheses.map((s) => s.value);
      if (thesisIds.length === 0) return;

      try {
        const res = await authApis().get(endpoints["lecturers-allowed"], {
          params: { thesisIds: thesisIds.join(",") },
        });
        setLecturers(res.data || []);
      } catch (ex) {
        setErr("Lỗi khi tải giảng viên được phép.");
      }
    };
    fetchAllowedLecturers();
  }, [selectedTheses]);

  const lecturerOptions = useMemo(
    () => (lecturers || []).map((l) => ({
      value: l.id,
      label: `${l.firstName} ${l.lastName}`,
    })),
    [lecturers]
  );

  const validate = () => {
    if (!name.trim()) return "Vui lòng nhập tên hội đồng!";
    if (!defenseDate) return "Vui lòng chọn ngày bảo vệ!";
    if (selectedTheses.length === 0 || selectedTheses.length > 5)
      return "Chọn ít nhất 1 và tối đa 5 khoá luận!";
    if (selectedLecturers.length < 3 || selectedLecturers.length > 5)
      return "Chọn 3–5 giảng viên!";
    return null;
  };

  const submit = async (e) => {
    e.preventDefault();
    setMsg(null);
    setErr(null);

    const v = validate();
    if (v) {
      setErr(v);
      return;
    }

    try {
      setLoading(true);
      const payload = {
        name,
        defenseDate,
        memberIds: selectedLecturers.map((s) => s.value),
        thesisIds: selectedTheses.map((s) => s.value),
      };

      const res = await authApis().post(endpoints["committees"], payload);
      setMsg("Tạo hội đồng thành công!");
      setName("");
      setDefenseDate("");
      setSelectedTheses([]);
      setLecturers([]);
      setSelectedLecturers([]);
    } catch (ex) {
      setErr("Lỗi khi tạo hội đồng.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mt-4">
      <Card className="shadow">
        <Card.Body>
          <h2 className="text-primary mb-4">Tạo Hội Đồng Bảo Vệ</h2>
          
          {msg && <Alert variant="success" dismissible onClose={() => setMsg(null)}>{msg}</Alert>}
          {err && <Alert variant="danger" dismissible onClose={() => setErr(null)}>{err}</Alert>}

          <Form onSubmit={submit}>
            <Row className="g-3">
              <Col md={6}>
                <Form.Group>
                  <Form.Label>Tên hội đồng</Form.Label>
                  <Form.Control
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    placeholder="VD: Hội đồng KLTN CNTT 01"
                    required
                  />
                </Form.Group>
              </Col>

              <Col md={6}>
                <Form.Group>
                  <Form.Label>Ngày bảo vệ</Form.Label>
                  <Form.Control
                    type="datetime-local"
                    value={defenseDate}
                    onChange={(e) => setDefenseDate(e.target.value)}
                    required
                  />
                </Form.Group>
              </Col>
            </Row>

            <Form.Group className="mt-3">
              <Form.Label>Chọn khoá luận (tối đa 5)</Form.Label>
              <Select
                isMulti
                options={thesisOptions}
                value={selectedTheses}
                onChange={setSelectedTheses}
                placeholder="Chọn khoá luận"
                closeMenuOnSelect={false}
                className="basic-multi-select"
                classNamePrefix="select"
              />
            </Form.Group>

            <Form.Group className="mt-3">
              <Form.Label className="d-flex justify-content-between">
                <span>Chọn giảng viên (tối thiểu 3, tối đa 5)</span>
                <small className="text-muted">1) Chủ tịch, 2) Thư ký, 3) Phản biện</small>
              </Form.Label>
              <Select
                isMulti
                options={lecturerOptions}
                value={selectedLecturers}
                onChange={setSelectedLecturers}
                placeholder={selectedTheses.length ? "Chọn giảng viên theo thứ tự vai trò" : "Hãy chọn khoá luận trước"}
                isDisabled={!selectedTheses.length}
                closeMenuOnSelect={false}
                className="basic-multi-select"
                classNamePrefix="select"
              />
            </Form.Group>

            {selectedLecturers.length > 0 && (
              <Card className="mt-3 border-light bg-light">
                <Card.Body>
                  <Card.Title className="h6">Vai trò trong hội đồng</Card.Title>
                  {selectedLecturers.map((s, idx) => (
                    <Row key={s.value} className="g-2 align-items-center mb-2">
                      <Col md={6}>
                        <span className="fw-medium">{s.label}</span>
                      </Col>
                      <Col md={6}>
                        <span className="text-primary">{ROLE_PREVIEW[idx] || "Thành viên"}</span>
                      </Col>
                    </Row>
                  ))}
                </Card.Body>
              </Card>
            )}

            <div className="text-center mt-4">
              <Button 
                type="submit" 
                variant="primary" 
                size="lg"
                disabled={loading}
                className="px-5"
              >
                {loading ? "Đang xử lý..." : "Tạo hội đồng"}
              </Button>
            </div>
          </Form>
        </Card.Body>
      </Card>
    </div>
  );
};

export default CreateCommittee;
