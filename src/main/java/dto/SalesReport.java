package dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "sales_report")
public class SalesReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Integer reportId;

    //@Enumerated(EnumType.STRING)
    //@Column(name = "report_type", nullable = false)
    //private ReportType reportType; // Enum for report types

    @Column(name = "net_sales_price", nullable = false)
    private BigDecimal netSalesPrice;

    @Column(name = "report_date", nullable = false)
    private java.util.Date reportDate;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
