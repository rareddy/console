package org.jboss.as.console.client.shared.runtime.jpa;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.shared.help.HelpSystem;
import org.jboss.as.console.client.shared.runtime.Metric;
import org.jboss.as.console.client.shared.runtime.RuntimeBaseAddress;
import org.jboss.as.console.client.shared.runtime.charts.Column;
import org.jboss.as.console.client.shared.runtime.charts.NumberColumn;
import org.jboss.as.console.client.shared.runtime.charts.TextColumn;
import org.jboss.as.console.client.shared.runtime.jpa.model.JPADeployment;
import org.jboss.as.console.client.shared.runtime.plain.PlainColumnView;
import org.jboss.as.console.client.shared.viewframework.builder.SimpleLayout;
import org.jboss.ballroom.client.widgets.tools.ToolButton;
import org.jboss.ballroom.client.widgets.tools.ToolStrip;
import org.jboss.dmr.client.ModelDescriptionConstants;
import org.jboss.dmr.client.ModelNode;

/**
 * @author Heiko Braun
 * @date 1/19/12
 */
public class BasicMetrics {

    private PlainColumnView txSampler;
    private JPAMetricPresenter presenter;
    private JPADeployment currentUnit;
    private PlainColumnView querySampler;
    private PlainColumnView queryExecSampler;

    public BasicMetrics(JPAMetricPresenter presenter) {
        this.presenter = presenter;
    }

    Widget asWidget() {

        final ToolStrip toolStrip = new ToolStrip();
        toolStrip.addToolButtonRight(new ToolButton(Console.CONSTANTS.common_label_refresh(), new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // TODO
            }
        }));

        //  ------

        NumberColumn txCount = new NumberColumn("completed-transaction-count","Completed");

        Column[] cols = new Column[] {
                txCount.setBaseline(true),
                new NumberColumn("successful-transaction-count","Successful").setComparisonColumn(txCount)

        };


        /*final HelpSystem.AddressCallback addressCallback = new HelpSystem.AddressCallback() {
            @Override
            public ModelNode getAddress() {
                ModelNode address = new ModelNode();
                address.get(ModelDescriptionConstants.ADDRESS).set(RuntimeBaseAddress.get());
                address.get(ModelDescriptionConstants.ADDRESS).add("subsystem", "jpa");
                address.get(ModelDescriptionConstants.ADDRESS).add("hibernate-persistence-unit", "*");
                return address;
            }
        };*/

        txSampler = new PlainColumnView("Transactions", null)
                .setColumns(cols)
                .setWidth(100, Style.Unit.PCT);


        //  ------

        NumberColumn queryCount = new NumberColumn("query-cache-put-count","Query Put Count");

        Column[] queryCols = new Column[] {
                queryCount.setBaseline(true),
                new NumberColumn("query-cache-hit-count","Query Hit Count").setComparisonColumn(queryCount),
                new NumberColumn("query-cache-miss-count","Query Miss Count").setComparisonColumn(queryCount)

        };

        querySampler  = new PlainColumnView("Query Cache", null)
                .setColumns(queryCols)
                .setWidth(100, Style.Unit.PCT);


        //  ------

        NumberColumn queryExecCount = new NumberColumn("query-execution-count","Query Execution Count");

        Column[] queryExecCols = new Column[] {
                queryExecCount,
                new NumberColumn("query-execution-max-time","Exec Max Time"),
                new TextColumn("query-execution-max-time-query-string","Max Time Query")

        };

        queryExecSampler  = new PlainColumnView("Query Execution", null)
                .setColumns(queryExecCols)
                .setWidth(100, Style.Unit.PCT);




        // ----

        SimpleLayout layout = new SimpleLayout()
                .setPlain(true)
                .setTopLevelTools(toolStrip.asWidget())
                .setHeadline("Persistence Unit Metrics")
                .setDescription("Metrics for a persistence unit.")
                .addContent("Transactions", txSampler.asWidget())
                .addContent("Query Execution", queryExecSampler.asWidget())
                .addContent("Query Cache", querySampler.asWidget());



        // test data
        txSampler.addSample(new Metric(
                20l, 19l
        ));


        queryExecSampler.addSample(new Metric(
                "10", "3", "select u from User u"

        ) );


        return layout.build();
    }

    public void setUnit(JPADeployment unit) {
        this.currentUnit = unit;
    }
}
