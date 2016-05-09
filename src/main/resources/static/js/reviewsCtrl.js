bulkAppControllers.controller("reviewsCtrl", function($scope, $rootScope, $http, $stateParams) {
	$scope.curTitle = "Reviews";
	var profileId = $stateParams.userId;

	$.post("/profile", {id: profileId}, function(responseJSON) {
		responseObject = JSON.parse(responseJSON);
		//$scope.reviews = responseObject.reviews;
		console.log(responseObject.reviews);
	});

	$scope.reviews = [{rating: 4, text: "HE COOL YO"},
	                  {rating: 1, text: "Didn't show up"}];

	
});