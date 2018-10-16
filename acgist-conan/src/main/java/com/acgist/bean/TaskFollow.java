package com.acgist.bean;

import java.util.List;

/**
 * 任务结果
 */
public class TaskFollow {

	private String value;
	private Long times;

	public TaskFollow() {
	}

	public TaskFollow(String value, Long times) {
		this.value = value;
		this.times = times;
	}

	private void addTimes(Long times) {
		this.times += times;
	}

	/**
	 * 合并
	 */
	public static final void combine(List<TaskFollow> ori, List<TaskFollow> follow) {
		if(ori == null || follow == null) {
			return;
		}
		follow.forEach(item -> {
			int index = ori.indexOf(item);
			if(index == -1) {
				ori.add(item);
			} else {
				ori.get(index).addTimes(item.getTimes());
			}
		});
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getTimes() {
		return times;
	}

	public void setTimes(Long times) {
		this.times = times;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(obj instanceof TaskFollow) {
			TaskFollow tmp = (TaskFollow) obj;
			return this.value.equals(tmp.value);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.value.hashCode();
	}
	
}
