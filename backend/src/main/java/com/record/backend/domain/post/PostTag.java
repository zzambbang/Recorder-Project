package com.record.backend.domain.post;

import static javax.persistence.FetchType.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.record.backend.domain.tag.Tag;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class PostTag {

	@Id @GeneratedValue
	@Column(name = "post_tag_id")
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "tag_id")
	private Tag tag;
}
