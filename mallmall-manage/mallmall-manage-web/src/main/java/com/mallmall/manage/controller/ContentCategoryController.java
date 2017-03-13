package com.mallmall.manage.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mallmall.manage.pojo.Content;
import com.mallmall.manage.pojo.ContentCategory;
import com.mallmall.manage.pojo.ItemCat;
import com.mallmall.manage.service.ContentCategoryService;

@RequestMapping("content/category")
@Controller
public class ContentCategoryController {

	@Autowired
	private ContentCategoryService contentCategoryService;
	
	/**
	 * 查询内容分类
	 * 
	 * @param parentId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<ContentCategory>> queryContentCategoryList(
			@RequestParam(value = "id", defaultValue = "0") Long parentId) {

		try {
			ContentCategory contentCategory = new ContentCategory();
			contentCategory.setParentId(parentId);
			List<ContentCategory> contentCategories = this.contentCategoryService.queryByWhere(contentCategory);
			// 资源不存在 404
			if (contentCategories == null || contentCategories.isEmpty())
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			// 200
			return ResponseEntity.ok(contentCategories);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	/**
	 * 保存
	 * 
	 * @param contentCategory
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ContentCategory> saveContentCategory(ContentCategory contentCategory) {
		
		try {
			contentCategory.setIsParent(false);
			this.contentCategoryService.save(contentCategory);
			ContentCategory parent = this.contentCategoryService.queryById(contentCategory.getParentId());
			if (!parent.getIsParent()) {
				parent.setIsParent(true);
				this.contentCategoryService.updateSelective(parent);
			}
			// 204
			return ResponseEntity.status(HttpStatus.CREATED).body(contentCategory);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	/**
	 * 重命名
	 * 
	 * @param contentCategory
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<Void> updateContentCategory(ContentCategory contentCategory) {
		try {
			this.contentCategoryService.updateSelective(contentCategory);
			// 204
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	/**
	 * delete
	 * 
	 * @param contentCategory
	 * @return
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteContentCategory(ContentCategory contentCategory) {
		try {
			List<Object> ids = new ArrayList<Object>();
			ids.add(contentCategory.getId());
			findAllSubNode(contentCategory.getId(),ids);
			this.contentCategoryService.deleteByIds(ids, "id", ContentCategory.class);
			
			//判断此父节点是否还包含其它子节点
			ContentCategory param = new ContentCategory();
			param.setParentId(contentCategory.getParentId());
			List<ContentCategory> list = this.contentCategoryService.queryByWhere(param);
			if(null == list || list.isEmpty()){
				//没有其它子节点
				ContentCategory parent = new ContentCategory();
				parent.setParentId(contentCategory.getParentId());
				parent.setIsParent(false);
				this.contentCategoryService.updateSelective(parent);
			}
			
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	/**
	 * 查询所有子节点并放入集合
	 * 
	 * @param parentId
	 * @param ids
	 */
	private void findAllSubNode(Long parentId, List<Object> ids) {
		ContentCategory param = new ContentCategory();
		param.setParentId(parentId);
		List<ContentCategory> list = this.contentCategoryService.queryByWhere(param);
		for (ContentCategory contentCategory : list) {
			ids.add(contentCategory.getId());
			if(contentCategory.getIsParent()){
				findAllSubNode(contentCategory.getId(), ids);
			}
		}
	}

}
