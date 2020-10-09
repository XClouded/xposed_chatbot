package anet.channel.monitor;

class NetWorkKalmanFilter {
    private static final String TAG = "speed.NetWorkKalmanFilter";
    private double Kalman_C1 = 0.0d;
    private double Kalman_C2 = 0.0d;
    private long Kalman_Count = 0;
    private double Kalman_ek;
    private double Kalman_z;
    private double kalman_Kk;
    private double kalman_Pk;
    private double kalman_Q;
    private double kalman_R;
    private double kalman_Xk;
    private double mcurrentNetWorkSpeed = 0.0d;

    NetWorkKalmanFilter() {
    }

    public double addMeasurement(double d, double d2) {
        double d3 = d / d2;
        if (d3 >= 8.0d) {
            if (this.Kalman_Count == 0) {
                this.Kalman_C1 = d3;
                this.kalman_Xk = this.Kalman_C1;
                this.kalman_R = this.kalman_Xk * 0.1d;
                this.kalman_Q = this.kalman_Xk * 0.02d;
                this.kalman_Pk = this.kalman_Xk * 0.1d * this.kalman_Xk;
            } else if (this.Kalman_Count == 1) {
                this.Kalman_C2 = d3;
                this.kalman_Xk = this.Kalman_C2;
            } else {
                double d4 = d3 - this.Kalman_C2;
                this.Kalman_C1 = this.Kalman_C2;
                this.Kalman_C2 = d3;
                this.Kalman_z = d3 / 0.95d;
                this.Kalman_ek = this.Kalman_z - (this.kalman_Xk * 0.95d);
                char c = 0;
                double sqrt = Math.sqrt(this.kalman_R);
                if (this.Kalman_ek >= 4.0d * sqrt) {
                    this.Kalman_ek = (this.Kalman_ek * 0.75d) + (sqrt * 2.0d);
                    c = 1;
                } else if (this.Kalman_ek <= -4.0d * sqrt) {
                    this.Kalman_ek = (sqrt * -1.0d) + (this.Kalman_ek * 0.75d);
                    c = 2;
                }
                this.kalman_R = Math.min(Math.max(Math.abs((this.kalman_R * 1.05d) - ((this.Kalman_ek * 0.0025d) * this.Kalman_ek)), this.kalman_R * 0.8d), this.kalman_R * 1.25d);
                this.kalman_Kk = this.kalman_Pk / ((0.9025d * this.kalman_Pk) + this.kalman_R);
                this.kalman_Xk = this.kalman_Xk + (1.0526315789473684d * d4) + (this.kalman_Kk * this.Kalman_ek);
                if (c == 1) {
                    this.kalman_Xk = Math.min(this.kalman_Xk, this.Kalman_z);
                } else if (c == 2) {
                    this.kalman_Xk = Math.max(this.kalman_Xk, this.Kalman_z);
                }
                this.kalman_Pk = (1.0d - (0.95d * this.kalman_Kk)) * (this.kalman_Pk + this.kalman_Q);
            }
            if (this.kalman_Xk < 0.0d) {
                this.mcurrentNetWorkSpeed = this.Kalman_C2 * 0.7d;
                this.kalman_Xk = this.mcurrentNetWorkSpeed;
            } else {
                this.mcurrentNetWorkSpeed = this.kalman_Xk;
            }
            return this.mcurrentNetWorkSpeed;
        } else if (this.Kalman_Count != 0) {
            return this.mcurrentNetWorkSpeed;
        } else {
            this.mcurrentNetWorkSpeed = d3;
            return this.mcurrentNetWorkSpeed;
        }
    }

    public void ResetKalmanParams() {
        this.Kalman_Count = 0;
        this.mcurrentNetWorkSpeed = 0.0d;
    }
}
