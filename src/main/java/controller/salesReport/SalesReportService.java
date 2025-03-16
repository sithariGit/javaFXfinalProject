package controller.salesReport;

import dto.SalesReport;

import java.sql.*;

public class SalesReportService {

    public boolean addSalesDetail(SalesReport salesReport, Connection connection){
        try {
            //Connection connection = DBConnection.getInstance().getConnection();
            String sql="INSERT INTO sales_report(net_sales_price, report_date, order_id, user_id) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setBigDecimal(1,salesReport.getNetSalesPrice());
            preparedStatement.setDate(2, (Date) salesReport.getReportDate());
            preparedStatement.setInt(3,salesReport.getOrder().getOrderId());
            preparedStatement.setInt(4,salesReport.getUser().getUserId());

            boolean insertedReport = preparedStatement.executeUpdate() > 0;
            return true;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
