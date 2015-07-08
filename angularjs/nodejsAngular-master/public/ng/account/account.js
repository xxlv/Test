angular.module('SharendaApp.Account',[])
.config(['$routeProvider',function($routeProvider) {
	$routeProvider
	.when('/',{
		controller: 'AccountController',
		templateUrl: BASE_URL+'/ng/account/account.tpl.html'
	});
}])
.controller('AccountController',['$scope','CategoryService','AlertService','EventService','ContactService','UserService',function($scope,CategoryService,AlertService,EventService,ContactService,UserService) {
	$scope.timeOptions = [];
	$scope.appointment = {};
	$scope.category = {};
	$scope.Contacts = [];
	$scope.userPurpose = {}; // naveen
	$scope.questions = {};
	$scope.loadEventData = function(event_id) {
		event_data = {};
		angular.forEach($scope.userEvents,function(event) {
			if(event.id == event_id) {
				event_data = event;
			}
		});
		time = moment(event_data.start_datetime).format('HH.mm');
		$scope.appointment = {
			purpose: event_data.purpose,
			id: event_data.id,
			client_id: [event_data.client_id],
			category_id: [event_data.category_id],
			time: [time],
			start: moment(event_data.start_datetime).format('DD/MM/YYYY'),
			duration: parseInt(event_data.duration),
			description: event_data.description,
			status:event_data.status,
		};
		$scope.appointment.question = [];
		angular.forEach(event_data.question,function(question) {
			angular.forEach($scope.questions,function(row){
				if(question.question_id == row.id && row.type == "list"){
					angular.forEach(row.option,function(option){
						if(option.option == question.answer){
							$scope.appointment.question[question.question_id] = option.id;
						}
					});
				}else if(question.question_id == row.id){
					$scope.appointment.question[question.question_id] = question.answer;
				}
			});
		});
		$scope.$apply();
		$scope.event.$submitted = false;
	};
	$scope.$watch('user',function() {
		if($scope.user.role == 'client'){
			window.location.href = $scope.ACCOUNT_URL+'professionnel';
		}
		if($scope.user.role == 'professionnel'){
			$scope.$emit('createEvent',0);
			UserService.getInfo(function(data) {
				$scope.questions = data.questions;
				$scope.userPurpose = data.purposes.purpose; // naveen
				$scope.userCategories = data.details.categories;
			});
			ContactService.getContact(function(data) {
				$scope.Contacts = data;
			});
		}
	});
	timefunction = function(num,state){
		options = [];
		start = 0;
		end = 24;
		for(var i = start; i < end; i+=0.15) {
			time = i.toFixed(2);
			min = (time%1).toFixed(2);
			if(min == 0.60) {
				i+= 0.40;
				val = i.toFixed(2);
				if(parseFloat(val) < 10){
					op = float2int(val)+":"+val.split(".")[1];
					options.push({value:'0'+val,text:op});
				}else{
					op = float2int(val)+":"+val.split(".")[1];
					options.push({value:val,text:op});
				}
				continue;
			};
			if(parseFloat(time) < 10){
				op = float2int(time)+":"+time.split(".")[1];
				options.push({value:'0'+time,text:op});
			}else{
				op = float2int(time)+":"+time.split(".")[1];
				options.push({value:time,text:op});
			}
		}
		options.pop();
		return options;
	};
	float2int = function(value) {
		return value | 0;
	};
	$scope.addcategory = function(data){
		if(data.name != null && data.color != null){
			CategoryService.createCategory(data,function(res) {
				if(res.__all__.status){
					AlertService.success(res.__all__.message);
				}else if(!res.__all__.status){
					AlertService.error(res.errors);
				}
				$('#CategoryModel').modal('hide');
				$scope.$emit('createCategory',res);
			});
		}
	};
	$scope.createEvent = function(e) {
		var valid = true;
		if(typeof(e.client_id) == undefined || e.client_id == null){
			alert('Sélectionnez Contact');
			$('#client').focus();
			valid = false;
		}
		if(typeof(e.category_id) == undefined || e.category_id == null && valid){
			alert('Sélectionnez Catégorie');
			$('#category').focus();
			valid = false;
		}
		if(typeof(e.time) == undefined || e.time == null && valid){
			alert('Sélectionnez heure');
			$('#timeDrop').focus();
			valid = false;
		}
		if(valid){
			if(e.description == null){
				e.description = "N/A";
			}
			$scope.event.$submitted = true;
			EventService.eventAvailability(e,function(res) {
				$('body').addClass('disable-page');
				if(res.overrite){
					$('body').removeClass('disable-page');
					var r = confirm("Click OK to Cancel Overlapped Appointments");
					if(r==true){
						e.overrite = true;
						$('body').addClass('disable-page');
						EventService.createEvent(e,function(res) {
							if(res.__all__.status){
								AlertService.success(res.__all__.message);
								// $scope.event.$submitted = true;
								$('#eventModal').modal('hide');
								$scope.appointment = {};
							}else if(!res.__all__.status){
								AlertService.error(res.errors);
							}
							$scope.$emit('createEvent',res);
							$('body').removeClass('disable-page');
						});
						$('#eventModal').modal('hide');
					}else{
						$('#eventModal').modal('hide');
						$('body').removeClass('disable-page');
					}
				}else{
					EventService.createEvent(e,function(res) {
						if(res.__all__.status){
							AlertService.success(res.__all__.message);
							// $scope.event.$submitted = true;
							$('#eventModal').modal('hide');
							$scope.appointment = {};
						}else if(!res.__all__.status){
							AlertService.error(res.errors);
						}
						$scope.$emit('createEvent',res);
						$('body').removeClass('disable-page');
					});
				}
			});
		}
	};
	$scope.deleteEvent = function(id){
		$('body').addClass('disable-page');
		EventService.deleteEvent(id,function(res) {
			$('body').removeClass('disable-page');
			if(res.__all__.status){
				AlertService.success(res.__all__.message);
				$('#updateeventModal').modal('hide');
			}else if(!res.__all__.status){
				AlertService.error(res.errors);
			}
			$scope.$emit('createEvent',res);
		});
	};
	$scope.updateEvent = function(data){
		id = data.id;
		$('body').addClass('disable-page');
		EventService.checkAvailability(id,data,function(res) {
			if(res.overrite){
				$('body').removeClass('disable-page');
				var r = confirm("Click OK to Cancel Overlapped Appointments");
				if(r==true){
					$('body').addClass('disable-page');
					data.overrite = true;
					EventService.updateEvent(id,data,function(res) {
						$('body').removeClass('disable-page');
						if(res.__all__.status){
							AlertService.success(res.__all__.message);
							$('#updateeventModal').modal('hide');
						}else if(!res.__all__.status){
							AlertService.error(res.errors);
						}
						$scope.$emit('createEvent',res);
					});
					$('#updateeventModal').modal('hide');
				}else{
					$('#updateeventModal').modal('hide');
				}
			}else{
				data.overrite = false;
				EventService.updateEvent(id,data,function(res) {
					$('body').removeClass('disable-page');
					if(res.__all__.status){
						AlertService.success(res.__all__.message);
						$('#updateeventModal').modal('hide');
					}else if(!res.__all__.status){
						AlertService.error(res.errors);
					}
					$scope.$emit('createEvent',res);
				});
			}
		});
	};
	$scope.timeOptions = timefunction(0,0);
	$scope.timeConfig = {
		valueField: 'value',
		labelField: 'text',
		sort: false,
		sortField: [],
		placeholder: 'heure',
		maxItems: 1,	
	};
	$scope.categoryChange = function(id){
		if(!$scope.event.$submitted){
			angular.forEach($scope.userCategories,function(cate) {
				if(id == cate.id) {
					$scope.appointment.duration = parseInt(cate.duration);
				}
			});
		}
	};
}]);