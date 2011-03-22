package org.jboss.as.console.client.debug;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.as.console.client.core.SuspendableViewImpl;
import org.jboss.as.console.client.shared.dispatch.InvocationMetrics;
import org.jboss.as.console.client.shared.model.DeploymentRecord;
import org.jboss.as.console.client.widgets.ContentGroupLabel;
import org.jboss.as.console.client.widgets.RHSContentPanel;
import org.jboss.as.console.client.widgets.tables.DefaultCellTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Heiko Braun
 * @date 3/22/11
 */
public class InvocationMetricsView extends SuspendableViewImpl implements InvocationMetricsPresenter.MyView{
    private InvocationMetricsPresenter presenter;

    private CellTable<SimpleMetric> invocationTable;

    @Override
    public void setPresenter(InvocationMetricsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public Widget createWidget() {

        LayoutPanel layout = new RHSContentPanel("Invocation Metrics");

        invocationTable = new DefaultCellTable<SimpleMetric>(100);
        TextColumn<SimpleMetric> type = new TextColumn<SimpleMetric>() {
            @Override
            public String getValue(SimpleMetric metric) {
                return metric.getKey();
            }
        };

        TextColumn<SimpleMetric> count = new TextColumn<SimpleMetric>() {

            @Override
            public void render(Cell.Context context, SimpleMetric object, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<div style='text-align:right'>")
                        .append(object.getValue())
                        .appendHtmlConstant("</div>");
            }

            @Override
            public String getValue(SimpleMetric metric) {
                return String.valueOf(metric.getValue());
            }
        };

        invocationTable.addColumn(type, "Type");
        invocationTable.addColumn(count, "Count");

        layout.add(new ContentGroupLabel("Invocation Count"));
        layout.add(invocationTable);

        return layout;
    }

    @Override
    public void updateFrom(List<SimpleMetric> values) {
        invocationTable.setRowCount(values.size());
        invocationTable.setRowData(values);
    }
}
