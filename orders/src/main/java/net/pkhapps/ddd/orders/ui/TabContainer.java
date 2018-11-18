package net.pkhapps.ddd.orders.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

import java.util.HashMap;
import java.util.Map;

public class TabContainer extends Div {

    private Map<Tab, Component> contentMap = new HashMap<>();
    private Tabs tabs;

    public TabContainer(Tabs tabs) {
        this.tabs = tabs;
        tabs.addSelectedChangeListener(event -> showTabContent(tabs.getSelectedTab()));
    }

    public void addTab(Tab tab, Component content) {
        contentMap.put(tab, content);
    }

    public void addTab(String label, Component content) {
        var tab = new Tab(label);
        tabs.add(tab);
        addTab(tab, content);
        if (tabs.getSelectedTab() == tab) {
            showTabContent(tab);
        }
    }

    private void showTabContent(Tab tab) {
        removeAll();
        var content = contentMap.get(tab);
        if (content != null) {
            add(content);
        }
    }
}
