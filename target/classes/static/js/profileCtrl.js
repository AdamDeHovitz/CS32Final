bulkAppControllers.controller("profileCtrl", function($scope, $http, $stateParams) {
	var profileId = $stateParams.userId;

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