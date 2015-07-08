angular.module('NaveenApp',[
	'ngRoute',
	'ngCookies',
	'ngLocale',
	'angularFileUpload',
	'selectize',
])
.controller('ApplicationController',['$scope','AlertService','$location', function($scope, AlertService , $location) {
	$scope.currentPath = '/';
	$scope.$on('$routeChangeStart', function(next, current) {
		$scope.currentPath = $location.path()
	});
}]);