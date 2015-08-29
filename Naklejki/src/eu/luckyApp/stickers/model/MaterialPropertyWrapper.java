package eu.luckyApp.stickers.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MaterialPropertyWrapper {

	// private Material material;

	private StringProperty indexId;
	private StringProperty indexName;
	private StringProperty indexUnit;
	private StringProperty indexStore;
	private DoubleProperty indexAmount;
	private StringProperty additionalDescribe;

	public MaterialPropertyWrapper(Material m) {
		this.indexId = new SimpleStringProperty(m.getIndexId());
		this.indexName = new SimpleStringProperty(m.getIndexName());
		this.indexUnit = new SimpleStringProperty(m.getIndexUnit());
		this.indexStore = new SimpleStringProperty(m.getIndexStore());
		this.indexAmount = new SimpleDoubleProperty(m.getIndexAmount());
		this.additionalDescribe = new SimpleStringProperty(m.getAdditionalDescibe());
	}

	public Material getMaterial() {
		return new Material(indexId.get(), indexName.get(), indexUnit.get(), indexStore.get(),
				indexAmount.get());
	}

	public String getAdditionalDescibe() {
		return additionalDescribe.get();
	}

	public void setAdditionalDescibe(String additionalDescibe) {
		this.additionalDescribe.set(additionalDescibe);

	}

	public StringProperty additionalDescibeProperty() {
		return additionalDescribe;
	}

	public Double getIndexAmount() {
		return indexAmount.get();
	}

	public void setIndexAmount(Double indexAmount) {
		this.indexAmount.set(indexAmount);

	}

	public DoubleProperty indexAmountProperty() {
		return indexAmount;
	}

	public String getIndexId() {
		return indexId.get();
	}

	public void setIndexId(String indexId) {
		this.indexId.set(indexId);
	}

	public StringProperty indexIdProperty() {
		return indexId;
	}

	public String getIndexName() {
		return indexName.get();
	}

	public void setIndexName(String indexName) {
		this.indexName.set(indexName.replace(";", ":"));
	}

	public StringProperty indexNameProperty() {
		return indexName;
	}

	public String getIndexStore() {
		return indexStore.get();
	}

	public void setIndexStore(String indexStore) {
		this.indexStore.set(indexStore);

	}

	public StringProperty indexStoreProperty() {
		return indexStore;
	}

	public String getIndexUnit() {
		return indexUnit.get();
	}

	public void setIndexUnit(String indexUnit) {
		this.indexUnit.set(indexUnit);
	}

	public StringProperty indexUnitProperty() {
		return indexUnit;
	}
}
