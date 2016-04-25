<<<<<<< HEAD
bulkAppControllers.controller("reviewsCtrl", function($scope, $rootScope, $http, $stateParams) {
	$scope.curTitle = "Reviews";
	var profileId = $stateParams.userId;

	$.post("/profile", {id: profileId}, function(responseJSON) {
		responseObject = JSON.parse(responseJSON);
		//$scope.reviews = responseObject.reviews;
		console.log(responseObject.reviews);
	});

	$scope.reviews = [{rating: 4, text: "HE COOL YO"},
	                  {rating: 1, text: "dis bitch fucked me over"}];

=======
bulkAppControllers.controller("reviewsCtrl", function($scope, $rootScope, $http) {
	$scope.curTitle = "Reviews";
	var profileId = $stateParams.userId;

	$.post("/reviews", {id: profileId}, function(responseJSON) {
			responseObject = JSON.parse(responseJSON);
			$scope.curReviews = responseObject.reviews;
			console.log(responseObject);
	});

>>>>>>> 6ee9b243c73fac568a75d5c46fde5298ae4b4609
	

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