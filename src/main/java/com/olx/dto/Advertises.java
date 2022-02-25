package com.olx.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class Advertises {
	private int id;
	private String title;
	private double price;
	private int categoryId;
	private String description;
	private String username;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;
	private int statusId;
	private String postedBy;
	private String category;
	private String status;

	public Advertises() {
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public String getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(String postedBy) {
		this.postedBy = postedBy;
	}

	@Override
	public String toString() {
		return "AdvertisesRequest [id=" + id + ", title=" + title + ", price=" + price + ", categoryId=" + categoryId
				+ ", description=" + description + ", username=" + username + ", createdDate=" + createdDate
				+ ", modifiedDate=" + modifiedDate + ", status=" + statusId + ", postedBy=" + postedBy + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(category, categoryId, createdDate, description, id, modifiedDate, postedBy, price, status,
				statusId, title, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Advertises other = (Advertises) obj;
		return Objects.equals(category, other.category) && categoryId == other.categoryId
				&& Objects.equals(createdDate, other.createdDate) && Objects.equals(description, other.description)
				&& id == other.id && Objects.equals(modifiedDate, other.modifiedDate)
				&& Objects.equals(postedBy, other.postedBy)
				&& Double.doubleToLongBits(price) == Double.doubleToLongBits(other.price)
				&& Objects.equals(status, other.status) && statusId == other.statusId
				&& Objects.equals(title, other.title) && Objects.equals(username, other.username);
	}

}
