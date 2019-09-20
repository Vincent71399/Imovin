package sg.edu.nus.imovin.Retrofit.Response;

import sg.edu.nus.imovin.Retrofit.Object.ErrorData;

public class SelectDeletePlanResponse {
    private String _status;
    private ErrorData _error;

    public String get_status() {
        return _status;
    }

    public void set_status(String _status) {
        this._status = _status;
    }

    public ErrorData get_error() {
        return _error;
    }

    public void set_error(ErrorData _error) {
        this._error = _error;
    }
}
