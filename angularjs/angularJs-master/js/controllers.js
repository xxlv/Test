var myApp=angular.module('myApp', []);


// WORKS WELL IF USING A SERVER
/*
myApp.controller('ListControllers',function($scope, $http){
    $http.get('js/data.json').success(function(data){
     $scope.tutors= data;
     
 
});

} );

*/


//NOW WITH A BROWSER
myApp.controller('MyController',function ($scope){
  
     $scope.tutorsOrder=1;  
      $scope.tutorsOrder2=1;
      $scope.$watch("tutorsOrder","doUpdate");

      $scope.doUpdate=function(){
         $scope.tutorsOrder2= $scope.tutorsOrder+"10";
      }
      $scope.tutors=


[
{
    "name":"Bede",
    "profile":"University",
    "course":"Maths",
    "photo":"photo1.jpg"
},
{
    "name":"Jackson",
    "profile":"School",
    "course":"Bio",
    "photo":"photo2.jpg"
},
{
    "name":"Cathy",
    "profile":"University",
    "course":"English",
    "photo":"photo3.jpg"
},
{
    "name":"Bosco",
    "profile":"School",
    "course":"Physics",
    "photo":"photo4.jpg"
},
{
    "name":"Charles",
    "profile":"School",
    "course":"Maths",
    "photo":"photo5.png"
},
{
    "name":"Janette",
    "profile":"University",
    "course":"Bio",
    "photo":"photo6.png"
},
{
    "name":"Gregory",
    "profile":"School",
    "course":"English",
    "photo":"photo7.png"
},
{
    "name":"Benito",
    "profile":"University",
    "course":"Physics",
    "photo":"photo8.png"
}
]

// End of JSON data
} );



