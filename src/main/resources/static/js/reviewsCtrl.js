bulkAppControllers.controller("reviewsCtrl", function($scope, $rootScope, $http) {
	$scope.curTitle = "Reviews";
	var profileId = $stateParams.userId;

	$.post("/reviews", {id: profileId}, function(responseJSON) {
			responseObject = JSON.parse(responseJSON);
			$scope.curReviews = responseObject.reviews;
			console.log(responseObject);
	});

	

		/*
		if (!$rootScope.userId) {
			$location.path("/");
		}
		var postData = $.param({
			json: JSON.stringify({
				userId: userId,
			})
		});
		$http.post("/feedEntries", postData).success(function(data, status) {
			$scope.events = data.entries;
		});*/
});