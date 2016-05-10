bulkAppControllers.controller("reviewsCtrl", function($scope, $rootScope, $http, $stateParams) {
	$scope.curTitle = "Reviews";
	var profileId = $stateParams.userId;
	$scope.Math = window.Math;

	$.post("/profile", {id: profileId}, function(responseJSON) {
		responseObject = JSON.parse(responseJSON);
		$scope.reviews = responseObject.reviews;
	});
	
});