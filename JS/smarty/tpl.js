// 正则表达式替换  输出html
function Template(tpl){
	//初始化变量
	//--------------------------------------------------------------------------------------------------------------
	// fn 通过函数模板 ,该函数将获得tpl的字符串，并将模板变量替换 将匹配到的变量使用model中的值替代
	// code 通过Function构造函数的字符串 	
	// re 正则语法匹配	

	//--------------------------------------------------------------------------------------------------------------

	var fn,match,
		code=['var r=[];\nvar _html=function(str){return str.replace(/&/g, \'&amp;\').replace(/"/g, \'&quot;\').replace(/\'/g, \'&#39;\').replace(/</g, \'&lt;\').replace(/>/g, \'&gt;\');}'],

		re=/\{\s*([a-zA-Z\.\_0-9()]+)(\s*\|\s*safe)?\s*\}/m, //匹配{hello.word} {  hello()  }
	
		addLine=function(text){
			//生成编译函数
			code.push('r.push(\''+text.replace(/\'/g,'\\\'').replace(/\n/g,'\\n').replace(/\r/,'\\r')+'\');');
		};
						
		while(match=re.exec(tpl)){

			if(match.index>0){
				addLine(tpl.slice(0,match.index)); //Today:
				// document.write(tpl.slice(0,match.index));//从开始到匹配到的地点
			}
			if(match[2]){
				code.push('r.push(String(this.'+match[1]+'));');
			}else{
				code.push('r.push(_html(String(this.'+match[1]+')));');
			}
			// document.write(tpl.slice(0,match.index)+"<br/>");//从开始到匹配到的地点
			tpl=tpl.substring(match.index+match[0].length);//{name}1111{name2} => 1111{name2}
		}
		addLine(tpl);//最后的<1/a>	
		code.push('return r.join(\'\');');
		fn = new Function(code.join('\n'));//通过预编译获取fn
		this.render=function(model){
			return fn.apply(model);//将使用model对象代替this对象，在我们生成的代码中使用this.就可以访问到model的数据
		}
	}
