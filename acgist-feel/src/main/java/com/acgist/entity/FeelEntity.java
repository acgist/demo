package com.acgist.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "tb_feel", indexes = {
	@Index(name = "index_feel_uid", columnList = "uid"),
	@Index(name = "index_feel_date", columnList = "date")
})
@GenericGenerator(name = "sequenceGenerator", strategy = "uuid")
//@SequenceGenerator(name = "sequenceGenerator", sequenceName = "tb_feel_sequence")
public class FeelEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String uid; // 用户标识
	private String date; // 日期
	private String face; // 表情
	private String feel; // 用户感受
	private String prediction; // 预测感受

	// 用户ID
	@Column(length = 50)
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	// 日期字符串：yyyyMMdd
	@Column(length = 8)
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	// 脸部数据
	@Lob
	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

	// 用户心情JSON数据：[\"开心\"]
	@Column(length = 512)
	public String getFeel() {
		return feel;
	}

	public void setFeel(String feel) {
		this.feel = feel;
	}

	// 预测心情JSON数据：[\"开心\"]
	@Column(length = 512)
	public String getPrediction() {
		return prediction;
	}

	public void setPrediction(String prediction) {
		this.prediction = prediction;
	}

}
