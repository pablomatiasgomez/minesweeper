package com.pablomatiasgomez.minesweeper.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.pablomatiasgomez.minesweeper.repository.ObjectIdDeserializer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.bson.types.ObjectId;

import javax.annotation.Nullable;


public abstract class BaseDbEntity {

	@Nullable
	@JsonSerialize(using = ToStringSerializer.class)
	@JsonDeserialize(using = ObjectIdDeserializer.class)
	@JsonAlias("_id")
	private ObjectId id;

	@Nullable
	public String getId() {
		return id != null ? id.toString() : null;
	}

	@Nullable
	@JsonIgnore
	public ObjectId getObjectId() {
		return id;
	}

	public void setId(@Nullable ObjectId id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BaseDbEntity that = (BaseDbEntity) o;
		return new EqualsBuilder()
				.append(id, that.id)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(7, 21)
				.append(id)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("id", id)
				.toString();
	}

}
