bulkAppControllers.controller("pendingEventsCtrl", function($scope, $http) {
	$scope.curTitle = "Pending Events";
<<<<<<< HEAD
=======
	$scope.curEvents = [];

	$.post("/event-pending", {id: $rootScope.account.id}, function(responseJSON) {
			responseObject = JSON.parse(responseJSON);
			console.log(responseObject);
	});
>>>>>>> 079153847f07588a4e903ace1bc217709c879daa

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