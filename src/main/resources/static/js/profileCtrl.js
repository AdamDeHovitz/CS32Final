bulkAppControllers.controller("profileCtrl", function($scope, $http, $stateParams, $state) {
	var profileId = $stateParams.userId;
	$scope.profileId = profileId;
	$scope.Math = window.Math;

	console.log($state.current.name);
	if ($state.current.name == "tab.events-profile") {
		console.log("events")
		$scope.curReviewUrl = "events-reviews";
	} else if ($state.current.name == "tab.account-profile") {
		console.log("account");
		$scope.curReviewUrl = "account-reviews";
	} else {
		$scope.curReviewUrl = "reviews";
	}
	console.log($scope.curReviewUrl);
	
	$.post("/profile", {id: profileId}, function(responseJSON) {
			responseObject = JSON.parse(responseJSON);
			$scope.profile = responseObject;
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