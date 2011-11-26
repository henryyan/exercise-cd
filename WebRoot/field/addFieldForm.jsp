<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<table align="center" style="width: 50%;background-color:black;" border="0" cellspacing="5" id='formTable'>
				<tr>
					<td class="label" style="width: 10%;">数量：</td>
					<td style="width: 20%">
						<input type="text" id="fieldTotal" name="fieldTotal"/>
					</td>
				</tr>

				<tr>
					<td class="label" style="width: 10%;">场地环境类型：</td>
					<td style="width: 20%">
						<div>
							<input type="radio" id="envType1" name="envType" value="室内" checked /><label for="envType1">室内</label>
							<input type="radio" id="envType2" name="envType" value="室外"/><label for="envType2">室外</label>
							<input type="radio" id="envType3" name="envType" value="半露天"/><label for="envType3">半露天</label>
						</div>
					</td>
				</tr>

				<tr>
					<td class="label" style="width: 10%;">可提前生成天数：</td>
					<td style="width: 20%">
						<input type="text" id="advance" name="advance" size="10" maxlength="3" value="30" />天
					</td>
				</tr>

				<tr>
					<td class="label" style="width: 10%;">可提前发布天数：</td>
					<td>
						<input type="text" id="issueAdvance" name="issueAdvance" size="10" maxlength="3" value="7" />天
					</td>
				</tr>

				<tr>
					<td>&nbsp;</td>
					<td>
						<input type='submit' name="submit" id='submit' title="保存" value="保 存" />
						<input type='button' id='resetBtn' validaor='true' title="重置表单" value="重 置" />
						<input type='button' id='backBtn' title="返回列表" value="返 回" />
					</td>
				</tr>
			</table>